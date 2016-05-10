//
// EvhListVideoConfAccountConfRecordResponse.m
//
#import "EvhListVideoConfAccountConfRecordResponse.h"
#import "EvhConfRecordDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListVideoConfAccountConfRecordResponse
//

@implementation EvhListVideoConfAccountConfRecordResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListVideoConfAccountConfRecordResponse* obj = [EvhListVideoConfAccountConfRecordResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _confRecords = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.confCount)
        [jsonObject setObject: self.confCount forKey: @"confCount"];
    if(self.confTimeCount)
        [jsonObject setObject: self.confTimeCount forKey: @"confTimeCount"];
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.confRecords) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhConfRecordDTO* item in self.confRecords) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"confRecords"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.confCount = [jsonObject objectForKey: @"confCount"];
        if(self.confCount && [self.confCount isEqual:[NSNull null]])
            self.confCount = nil;

        self.confTimeCount = [jsonObject objectForKey: @"confTimeCount"];
        if(self.confTimeCount && [self.confTimeCount isEqual:[NSNull null]])
            self.confTimeCount = nil;

        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"confRecords"];
            for(id itemJson in jsonArray) {
                EvhConfRecordDTO* item = [EvhConfRecordDTO new];
                
                [item fromJson: itemJson];
                [self.confRecords addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
