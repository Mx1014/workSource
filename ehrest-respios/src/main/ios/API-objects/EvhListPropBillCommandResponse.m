//
// EvhListPropBillCommandResponse.m
//
#import "EvhListPropBillCommandResponse.h"
#import "EvhPropBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPropBillCommandResponse
//

@implementation EvhListPropBillCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListPropBillCommandResponse* obj = [EvhListPropBillCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _members = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageOffset)
        [jsonObject setObject: self.nextPageOffset forKey: @"nextPageOffset"];
    if(self.members) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPropBillDTO* item in self.members) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"members"];
    }
    if(self.pageCount)
        [jsonObject setObject: self.pageCount forKey: @"pageCount"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageOffset = [jsonObject objectForKey: @"nextPageOffset"];
        if(self.nextPageOffset && [self.nextPageOffset isEqual:[NSNull null]])
            self.nextPageOffset = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"members"];
            for(id itemJson in jsonArray) {
                EvhPropBillDTO* item = [EvhPropBillDTO new];
                
                [item fromJson: itemJson];
                [self.members addObject: item];
            }
        }
        self.pageCount = [jsonObject objectForKey: @"pageCount"];
        if(self.pageCount && [self.pageCount isEqual:[NSNull null]])
            self.pageCount = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
