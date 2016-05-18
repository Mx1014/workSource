//
// EvhGetEarlyWarningLineCommand.m
//
#import "EvhGetEarlyWarningLineCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetEarlyWarningLineCommand
//

@implementation EvhGetEarlyWarningLineCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetEarlyWarningLineCommand* obj = [EvhGetEarlyWarningLineCommand new];
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
    if(self.warningLineType)
        [jsonObject setObject: self.warningLineType forKey: @"warningLineType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.warningLineType = [jsonObject objectForKey: @"warningLineType"];
        if(self.warningLineType && [self.warningLineType isEqual:[NSNull null]])
            self.warningLineType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
