//
// EvhUpgradeInfoResponse.m
//
#import "EvhUpgradeInfoResponse.h"
#import "EvhVersionDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpgradeInfoResponse
//

@implementation EvhUpgradeInfoResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpgradeInfoResponse* obj = [EvhUpgradeInfoResponse new];
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
    if(self.targetVersion) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.targetVersion toJson: dic];
        
        [jsonObject setObject: dic forKey: @"targetVersion"];
    }
    if(self.forceFlag)
        [jsonObject setObject: self.forceFlag forKey: @"forceFlag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"targetVersion"];

        self.targetVersion = [EvhVersionDTO new];
        self.targetVersion = [self.targetVersion fromJson: itemJson];
        self.forceFlag = [jsonObject objectForKey: @"forceFlag"];
        if(self.forceFlag && [self.forceFlag isEqual:[NSNull null]])
            self.forceFlag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
