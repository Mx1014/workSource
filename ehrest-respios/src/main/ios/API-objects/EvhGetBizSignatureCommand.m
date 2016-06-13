//
// EvhGetBizSignatureCommand.m
//
#import "EvhGetBizSignatureCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetBizSignatureCommand
//

@implementation EvhGetBizSignatureCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetBizSignatureCommand* obj = [EvhGetBizSignatureCommand new];
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
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.namespaceUserToken)
        [jsonObject setObject: self.namespaceUserToken forKey: @"namespaceUserToken"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.namespaceUserToken = [jsonObject objectForKey: @"namespaceUserToken"];
        if(self.namespaceUserToken && [self.namespaceUserToken isEqual:[NSNull null]])
            self.namespaceUserToken = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
