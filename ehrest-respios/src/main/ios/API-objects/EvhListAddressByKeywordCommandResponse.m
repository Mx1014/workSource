//
// EvhListAddressByKeywordCommandResponse.m
//
#import "EvhListAddressByKeywordCommandResponse.h"
#import "EvhAddressAddressDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListAddressByKeywordCommandResponse
//

@implementation EvhListAddressByKeywordCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListAddressByKeywordCommandResponse* obj = [EvhListAddressByKeywordCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _requests = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.requests) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhAddressAddressDTO* item in self.requests) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"requests"];
    }
    if(self.nextPageOffset)
        [jsonObject setObject: self.nextPageOffset forKey: @"nextPageOffset"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"requests"];
            for(id itemJson in jsonArray) {
                EvhAddressAddressDTO* item = [EvhAddressAddressDTO new];
                
                [item fromJson: itemJson];
                [self.requests addObject: item];
            }
        }
        self.nextPageOffset = [jsonObject objectForKey: @"nextPageOffset"];
        if(self.nextPageOffset && [self.nextPageOffset isEqual:[NSNull null]])
            self.nextPageOffset = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
