//
// EvhListPmsyPayerCommand.m
//
#import "EvhListPmsyPayerCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPmsyPayerCommand
//

@implementation EvhListPmsyPayerCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListPmsyPayerCommand* obj = [EvhListPmsyPayerCommand new];
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
    if(self.creatorId)
        [jsonObject setObject: self.creatorId forKey: @"creatorId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.creatorId = [jsonObject objectForKey: @"creatorId"];
        if(self.creatorId && [self.creatorId isEqual:[NSNull null]])
            self.creatorId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
