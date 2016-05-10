//
// EvhListAesUserKeyByUserResponse.m
//
#import "EvhListAesUserKeyByUserResponse.h"
#import "EvhAesUserKeyDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListAesUserKeyByUserResponse
//

@implementation EvhListAesUserKeyByUserResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListAesUserKeyByUserResponse* obj = [EvhListAesUserKeyByUserResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _aesUserKeys = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.aesUserKeys) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhAesUserKeyDTO* item in self.aesUserKeys) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"aesUserKeys"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"aesUserKeys"];
            for(id itemJson in jsonArray) {
                EvhAesUserKeyDTO* item = [EvhAesUserKeyDTO new];
                
                [item fromJson: itemJson];
                [self.aesUserKeys addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
