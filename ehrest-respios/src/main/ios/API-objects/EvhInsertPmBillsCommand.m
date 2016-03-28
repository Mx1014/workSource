//
// EvhInsertPmBillsCommand.m
// generated at 2016-03-25 19:05:19 
//
#import "EvhInsertPmBillsCommand.h"
#import "EvhUpdatePmBillsDto.h"

///////////////////////////////////////////////////////////////////////////////
// EvhInsertPmBillsCommand
//

@implementation EvhInsertPmBillsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhInsertPmBillsCommand* obj = [EvhInsertPmBillsCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _insertList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.insertList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhUpdatePmBillsDto* item in self.insertList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"insertList"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"insertList"];
            for(id itemJson in jsonArray) {
                EvhUpdatePmBillsDto* item = [EvhUpdatePmBillsDto new];
                
                [item fromJson: itemJson];
                [self.insertList addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
