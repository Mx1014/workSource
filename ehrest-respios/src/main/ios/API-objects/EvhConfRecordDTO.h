//
// EvhConfRecordDTO.h
// generated at 2016-04-01 15:40:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfRecordDTO
//
@interface EvhConfRecordDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* confId;

@property(nonatomic, copy) NSNumber* confDate;

@property(nonatomic, copy) NSNumber* confTime;

@property(nonatomic, copy) NSNumber* people;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

