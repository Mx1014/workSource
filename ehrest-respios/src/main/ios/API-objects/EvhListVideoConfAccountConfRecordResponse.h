//
// EvhListVideoConfAccountConfRecordResponse.h
// generated at 2016-04-07 10:47:32 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhConfRecordDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListVideoConfAccountConfRecordResponse
//
@interface EvhListVideoConfAccountConfRecordResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* confCount;

@property(nonatomic, copy) NSNumber* confTimeCount;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhConfRecordDTO*
@property(nonatomic, strong) NSMutableArray* confRecords;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

