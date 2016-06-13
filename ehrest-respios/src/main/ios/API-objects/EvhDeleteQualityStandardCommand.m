//
// EvhDeleteQualityStandardCommand.m
//
#import "EvhDeleteQualityStandardCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteQualityStandardCommand
//

@implementation EvhDeleteQualityStandardCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeleteQualityStandardCommand* obj = [EvhDeleteQualityStandardCommand new];
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
    if(self.standardId)
        [jsonObject setObject: self.standardId forKey: @"standardId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.standardId = [jsonObject objectForKey: @"standardId"];
        if(self.standardId && [self.standardId isEqual:[NSNull null]])
            self.standardId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
