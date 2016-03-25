//
// EvhListAesUserKeyByUserResponse.m
// generated at 2016-03-25 15:57:23 
//
#import "EvhListAesUserKeyByUserResponse.h"

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
