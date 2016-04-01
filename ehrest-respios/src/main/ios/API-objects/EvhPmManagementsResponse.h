//
// EvhPmManagementsResponse.h
// generated at 2016-04-01 15:40:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPmManagementsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmManagementsResponse
//
@interface EvhPmManagementsResponse
    : NSObject<EvhJsonSerializable>


// item type EvhPmManagementsDTO*
@property(nonatomic, strong) NSMutableArray* pmManagement;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

