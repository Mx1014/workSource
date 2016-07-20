//
// EvhDeleteWarningContactorCommand.m
//
#import "EvhDeleteWarningContactorCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteWarningContactorCommand
//

@implementation EvhDeleteWarningContactorCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeleteWarningContactorCommand* obj = [EvhDeleteWarningContactorCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.warningContactorId)
        [jsonObject setObject: self.warningContactorId forKey: @"warningContactorId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.warningContactorId = [jsonObject objectForKey: @"warningContactorId"];
        if(self.warningContactorId && [self.warningContactorId isEqual:[NSNull null]])
            self.warningContactorId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
