//
// EvhActivityListResponse.h
// generated at 2016-04-07 14:16:30 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhActivityDTO.h"
#import "EvhActivityMemberDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityListResponse
//
@interface EvhActivityListResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, strong) EvhActivityDTO* activity;

@property(nonatomic, copy) NSNumber* creatorFlag;

@property(nonatomic, copy) NSString* checkinQRUrl;

// item type EvhActivityMemberDTO*
@property(nonatomic, strong) NSMutableArray* roster;

@property(nonatomic, copy) NSNumber* nextAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

