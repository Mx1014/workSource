//
// EvhGetEnterpriseDetailByIdResponse.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import "EvhGetEnterpriseDetailByIdResponse.h"
#import "EvhEnterpriseDetailDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetEnterpriseDetailByIdResponse
//

@implementation EvhGetEnterpriseDetailByIdResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetEnterpriseDetailByIdResponse* obj = [EvhGetEnterpriseDetailByIdResponse new];
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
    if(self.detail) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.detail toJson: dic];
        
        [jsonObject setObject: dic forKey: @"detail"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"detail"];

        self.detail = [EvhEnterpriseDetailDTO new];
        self.detail = [self.detail fromJson: itemJson];
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
