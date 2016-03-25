//
// EvhListWebMenuResponse.m
// generated at 2016-03-25 15:57:23 
//
#import "EvhListWebMenuResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListWebMenuResponse
//

@implementation EvhListWebMenuResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListWebMenuResponse* obj = [EvhListWebMenuResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _menus = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.menus) {
        NSMutableArray* jsonArray = [NSMutableArray new];
