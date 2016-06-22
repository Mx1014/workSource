//
// EvhListDoorAccessQRKeyResponse.m
//
#import "EvhListDoorAccessQRKeyResponse.h"
#import "EvhDoorAccessQRKeyDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListDoorAccessQRKeyResponse
//

@implementation EvhListDoorAccessQRKeyResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListDoorAccessQRKeyResponse* obj = [EvhListDoorAccessQRKeyResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _keys = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.keys) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhDoorAccessQRKeyDTO* item in self.keys) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"keys"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"keys"];
            for(id itemJson in jsonArray) {
                EvhDoorAccessQRKeyDTO* item = [EvhDoorAccessQRKeyDTO new];
                
                [item fromJson: itemJson];
                [self.keys addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
